import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.util.Arrays;

public class RestCaller {

    public static void main(String[] args) {

        HttpHost targetHost = new HttpHost("https://url.com/");

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("name", "password");
        provider.setCredentials(AuthScope.ANY, credentials);

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
        cookie.setDomain("url.com");
        cookieStore.addCookie(cookie);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(provider);
        context.setAuthCache(authCache);
        context.setCookieStore(cookieStore);

        HttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .setDefaultCookieStore(cookieStore)
                .build();

        for (String s : args) {
            String url = "https://url.com/services/admin/XXX/parameter?type=one";

            String correctIdURL = url.replace("XXX", s);
            System.out.println(correctIdURL);

            try {
                HttpResponse response = client.execute(new HttpPost(correctIdURL), context);
                int statusCode = response.getStatusLine().getStatusCode();

                System.out.println("Call resulted in: " + statusCode);
            } catch (IOException e) {
                System.out.println(e.getMessage() + " , " + Arrays.toString(e.getStackTrace()));
            }
        }
    }
}

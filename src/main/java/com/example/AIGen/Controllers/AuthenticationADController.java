package com.example.AIGen.Controllers;



import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth/ad")
@CrossOrigin
public class AuthenticationADController {
//
//
//	 @Value("${azure.ad.client-id}")
//	    private String clientId;
//
//	    @Value("${azure.ad.client-secret}")
//	    private String clientSecret;
//
//	    @Value("${azure.ad.authority}")
//	    private String authority;
//
//	    @Value("${azure.ad.redirect-uri}")
//	    private String redirectUri;
//
//	    private ConfidentialClientApplication app;
//
//	    @PostConstruct
//	    public void init() throws Exception {
//	        app = ConfidentialClientApplication.builder(clientId, ClientCredentialFactory.createFromSecret(clientSecret))
//	                .authority(authority)
//	                .build();
//	    }
//
//	    @GetMapping("/auth/login")
//	    public String login() {
//	        AuthorizationRequestUrlParameters parameters = AuthorizationRequestUrlParameters
//	                .builder(redirectUri, Collections.singleton("User.Read"))
//	                .build();
//	        String authorizationUrl = app.getAuthorizationRequestUrl(parameters).toString();
//
//	        return "redirect:" + authorizationUrl;
//	    }
//
//	    @GetMapping("/auth/redirect")
//	    public String handleRedirect(@RequestParam("code") String authorizationCode, Model model) {
//	        try {
//	            IAuthenticationResult result = acquireTokenWithAuthorizationCode(authorizationCode);
//	            String accessToken = result.accessToken();
//
//	            User user = getUserProfile(accessToken);
//	            model.addAttribute("user", user);
//
//	            return "home"; // View template for successful login
//	        } catch (Exception ex) {
//	            ex.printStackTrace();
//	            return "error"; // View template for login error
//	        }
//	    }
//
//	    private IAuthenticationResult acquireTokenWithAuthorizationCode(String authorizationCode) throws Exception {
//	        CompletableFuture<IAuthenticationResult> future = app.acquireToken(
//	                AuthorizationCodeParameters.builder(authorizationCode, new URI(redirectUri))
//	                        .scopes(Collections.singleton("User.Read"))
//	                        .build());
//	        return future.join();
//	    }
//
//	    private User getUserProfile(String accessToken) {
//	        // Define the authentication provider
//	        IAuthenticationProvider authProvider = request -> {
//	            // Add the Authorization header to the HTTP request
//	            request.headers().add("Authorization", "Bearer " + accessToken);
//	            return CompletableFuture.completedFuture(null); // Return a completed future since it's synchronous
//	        };
//
//	        // Build the GraphServiceClient with the authentication provider
//	        GraphServiceClient<?> graphClient = GraphServiceClient
//	                .builder()
//	                .authenticationProvider(authProvider)
//	                .buildClient();
//
//	        // Fetch the user profile from Microsoft Graph API
//	        return graphClient.me().buildRequest().get();
//	    }
}

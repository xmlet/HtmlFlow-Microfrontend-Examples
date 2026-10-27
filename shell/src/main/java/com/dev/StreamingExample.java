package com.dev;

import htmlflow.HtmlFlow;
import htmlflow.HtmlView;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumTypeScriptType;

@Path("/pssr")
public class StreamingExample {

    private static final String HTMLFLOW_MFE_ENGINE =
            "https://cdn.jsdelivr.net/gh/xmlet/HtmlFlow@master/htmlflow-mfe-engine/dist/htmlflow-mfe-engine.js";

    private static final String SHELL = "http://localhost:8080";
    private static final String BIKE_SERVICE = "http://localhost:8081";
    private static final String CART_SERVICE = "http://localhost:8082";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        HtmlView<?> mfe = HtmlFlow.ViewFactory.builder().mfeEnabled(true).build().view((page -> {
            page.html()
                    .head()
                    .script().attrSrc(HTMLFLOW_MFE_ENGINE).attrType(EnumTypeScriptType.MODULE).__()
                    .link().addAttr("rel", "stylesheet").addAttr("href", SHELL + "/style.css").__()
                    .__()

                    .body()
                    .header().addAttr("class", "header_bar")
                    .div().img().addAttr("src", "bicycle.png").addAttr("style", "cursor: pointer; height:70px; width:70px;" ).addAttr("alt", "Bike Image").__().__()
                    .div().h1().text("Bicycles — Progressive streaming").__().__()
                    .nav().addAttr("class", "nav-style")
                    .div().img().addAttr("src", "shopping-cart.png").addAttr("alt", "Cart").__().__()
                    .div().img().addAttr("src", "orders.png").addAttr("alt", "Orders").__().__()
                    .div().img().addAttr("src", "logout.png").addAttr("alt", "Logout").__().__()
                    .__()
                    .__()

                    .div().addAttr("style", "display: flex;justify-content: space-around;")
                    .div()
                    .mfe((cfg)-> {
                        cfg.setMfeUrlResource(BIKE_SERVICE + "/bikes/stream");
                        cfg.setMfeName("mfe1");
                        cfg.setMfeListeningEventName("triggerBikeEvent");
                        cfg.setMfeTriggersEventName("triggerCartEvent");
                        cfg.setMfeStreamingData(true);
                        cfg.setMfeScriptUrl(BIKE_SERVICE + "/js/mfe-bikes.js");
                        cfg.setMfeStylingUrl(BIKE_SERVICE + "/css/style.css");
                    }).__()
                    .div()
                    .mfe((cfg)-> {
                        cfg.setMfeUrlResource(CART_SERVICE + "/cart/view");
                        cfg.setMfeName("mfe2");
                        cfg.setMfeListeningEventName("triggerCartEvent");
                        cfg.setMfeTriggersEventName("triggerOrderEvent");
                        cfg.setMfeScriptUrl(CART_SERVICE + "/mfe-cart.js");
                        cfg.setMfeStylingUrl(CART_SERVICE + "/style.css");
                    }).__()
                    .__()
                    .footer().addAttr("class", "footer")
                    .h3().text("© 2026 UAB Rental Service").__()
                    .__()
                    .__()
                    .__();
        }));

        String html = mfe.render();
        return Response.ok(html).build();

    }
}

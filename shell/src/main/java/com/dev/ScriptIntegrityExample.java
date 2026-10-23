package com.dev;

import htmlflow.HtmlFlow;
import htmlflow.HtmlView;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumTypeScriptType;

@Path("/sri")
public class ScriptIntegrityExample {

    private static final String HTMLFLOW_MFE_ENGINE =
            "https://cdn.jsdelivr.net/gh/xmlet/HtmlFlow@master/htmlflow-mfe-engine/dist/htmlflow-mfe-engine.js";

    // SRI hash of bike-service/src/main/resources/static/js/mfe-bikes.js
    private static final String BIKES_SCRIPT_INTEGRITY =
            "sha384-V2ag7/4EijBSAnk4OKfVF/m6RyGGLL/89NzKVAnTKNyZbBBQOgqjuBTELQsQETnv";


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        HtmlView<?> mfe = HtmlFlow.ViewFactory.builder().mfeEnabled(true).build().view(( page -> {
            page.html()
                    .head()
                    .script().attrSrc(HTMLFLOW_MFE_ENGINE).attrType(EnumTypeScriptType.MODULE).__()
                    .__()

                    .body()
                    .header().addAttr("class", "header_bar")
                    .div().img().addAttr("src", "bicycle.png").addAttr("style", "cursor: pointer; height:70px; width:70px;" ).addAttr("alt", "Bike Image").__().__()
                    .div().h1().text("Bicycles").__().__()
                    .nav().addAttr("class", "nav-style")
                    .div().img().addAttr("src", "shopping-cart.png").addAttr("alt", "Cart").__().__()
                    .div().img().addAttr("src", "orders.png").addAttr("alt", "Orders").__().__()
                    .div().img().addAttr("src", "logout.png").addAttr("alt", "Logout").__().__()
                    .__()
                    .__()

                    .div().addAttr("style", "display: flex;justify-content: space-around;")
                    .div()
                    .mfe((cfg)-> {
                        cfg.setMfeUrlResource("http://localhost:8081/bikes/view");
                        cfg.setMfeName("mfe1");
                        cfg.setMfeListeningEventName("triggerBikeEvent");
                        cfg.setMfeTriggersEventName("triggerCartEvent");
                        cfg.setMfeScriptUrl("http://localhost:8081/js/mfe-bikes.js");
                        cfg.setMfeStylingUrl("http://localhost:8081/css/style.css");
                        cfg.setMfeScriptIntegrity(BIKES_SCRIPT_INTEGRITY);
                    }).__()
                    .div()
                    .mfe((cfg)-> {
                        cfg.setMfeUrlResource("http://localhost:8082/cart/view");
                        cfg.setMfeName("mfe2");
                        cfg.setMfeListeningEventName("triggerCartEvent");
                        cfg.setMfeTriggersEventName("triggerOrderEvent");
                        cfg.setMfeScriptUrl("http://localhost:8082/mfe-cart.js");
                        cfg.setMfeStylingUrl("http://localhost:8082/style.css");
                    }).__()
                    .__()
                    .footer().addAttr("class", "footer")
                    .h3().text("© 2026 UAB Rental Service").__()
                    .__()
                    .__()
                    .__();
        }));
        return Response.ok(mfe.render()).build();
    }
}

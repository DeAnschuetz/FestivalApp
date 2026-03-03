package com.ffb.app.api;

import com.ffb.app.service.api.account.AccountService;
import com.ffb.app.service.api.cart.CartService;
import com.ffb.app.service.api.credit.CreditService;
import com.ffb.app.service.api.food.court.FoodCourtService;
import com.ffb.app.service.api.food.order.FoodOrderService;
import com.ffb.app.service.api.product.ProductService;
import com.ffb.app.service.impl.food.court.FoodCourtSimulationService;
import com.ffb.model.api.request.account.AccountRequest;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.credit.CreditAddRequest;
import com.ffb.model.api.request.food.court.FoodCourtRequestFull;
import com.ffb.model.api.request.product.ProductLinkRequest;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.ticket.TicketRequest;
import com.ffb.model.api.response.DatabaseResponse;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
@Path("master")
public class MasterEndpointImpl {

    // TODO Logging
    private final Logger LOG = LoggerFactory.getLogger(MasterEndpointImpl.class);

    /*
        LOGIN NUMBERS
    */
    private static final TicketRequest TICKET_LOGIN_NRS = new TicketRequest(//
        List.of(//
                "A-000-000-000",
            "V-000-000-001",
            "V-000-000-002",
            "V-000-000-003",
            "V-000-000-004",
            "V-000-000-005",
            "V-000-000-006",
            "V-000-000-007",
            "V-000-000-008",
            "V-000-000-009",
            "V-000-000-010",
            "F-000-000-001",
            "F-000-000-002",
            "F-000-000-003",
            "F-000-000-004",
            "F-000-000-005"
        )//
    );

    /*
        ACCOUNTS
    */
    private static final String STANDARD_PASSWORD = "1";
    private static final String A_000_000_000 = "A-000-000-000";
    private static final UUID A_000_000_000_ID = UUID.fromString("fe099c8c-06da-48b9-9c36-7df26322fe94");
    private static final String F_000_000_001 = "F-000-000-001";
    private static final UUID F_000_000_001_ID = UUID.fromString("e5a6778b-8576-4b4c-a4a3-97c5a3ac2cf0");
    private static final String F_000_000_002 = "F-000-000-002";
    private static final UUID F_000_000_002_ID = UUID.fromString("50c3ba6b-903e-4d00-81dd-2314c3ed478f");
    private static final String V_000_000_001 = "V-000-000-001";
    private static final UUID V_000_000_001_ID = UUID.fromString("484b61d1-064e-4f69-be9e-9cacd5a773f6");
    private static final String V_000_000_002 = "V-000-000-002";
    private static final UUID V_000_000_002_ID = UUID.fromString("dcddd091-cec9-4710-8cf2-c885d1b89e1f");
    private static final List<AccountRequest> ACCOUNT_REGISTER_REQUESTS = List.of(
        new AccountRequest(A_000_000_000_ID, A_000_000_000, STANDARD_PASSWORD),
        new AccountRequest(F_000_000_001_ID, F_000_000_001, STANDARD_PASSWORD),
        new AccountRequest(F_000_000_002_ID, F_000_000_002, STANDARD_PASSWORD),
        new AccountRequest(V_000_000_001_ID, V_000_000_001, STANDARD_PASSWORD),
        new AccountRequest(V_000_000_002_ID, V_000_000_002, STANDARD_PASSWORD)
    );

    /*
        FOOD COURTS
    */
    private static final UUID FOOD_COURT_1_ID = UUID.fromString("a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc");
    private static final String FOOD_COURT_1_NAME = "Burger Palace";
    private static final UUID FOOD_COURT_2_ID = UUID.fromString("e9b30a36-d946-4295-b934-9aec9014c8c1");
    private static final String FOOD_COURT_2_NAME = "Pizza Palace";
    private static final List<FoodCourtRequestFull> FOOD_COURT_REQUESTS = List.of(
        new FoodCourtRequestFull(FOOD_COURT_1_ID, F_000_000_001, FOOD_COURT_1_NAME),
        new FoodCourtRequestFull(FOOD_COURT_2_ID, F_000_000_002, FOOD_COURT_2_NAME)
    );

    /*
        PRODUCTS
    */
    private static final String BURGER_MENU_MIT_COLA = "Burger Menü mit Cola";
    private static final UUID BURGER_MENU_MIT_COLA_ID = UUID.fromString("88091439-2cc5-4e25-af0b-27fe6d393563");
    private static final double BURGER_MENU_MIT_COLA_PRICE = 12;
    private static final int BURGER_MENU_MIT_COLA_MINIMAL_WARNING = 10;
    private static final String BURGER_MENU_MIT_COLA_SYMBOL_IDENTIFIER = "TEST";
    private static final String BURGER_MENU = "Burger Menü";
    private static final UUID BURGER_MENU_ID = UUID.fromString("dbcb35ab-0e7e-4af0-8517-c2ab45897f18");
    private static final double BURGER_MENU_PRICE = 11;
    private static final int BURGER_MENU_MINIMAL_WARNING = 10;
    private static final String BURGER_MENU_SYMBOL_IDENTIFIER = "TEST";
    private static final String BURGER = "Burger";
    private static final UUID BURGER_ID = UUID.fromString("65de6f9f-c0b4-4038-896d-3bb80aa52434");
    private static final double BURGER_PRICE = 7.5;
    private static final int BURGER_MINIMAL_WARNING = 10;
    private static final String BURGER_SYMBOL_IDENTIFIER = "TEST";
    private static final String COLA = "Cola";
    private static final UUID COLA_ID = UUID.fromString("b9b0cc8f-53d3-40f7-9afe-d3c491ce2e75");
    private static final double COLA_PRICE = 7.5;
    private static final int COLA_MINIMAL_WARNING = 10;
    private static final String COLA_SYMBOL_IDENTIFIER = "TEST";
    private static final String POMMES = "Pommes";
    private static final UUID POMMES_ID = UUID.fromString("ea6e8266-b455-4136-9415-29ef8de05a15");
    private static final double POMMES_PRICE = 4;
    private static final int POMMES_MINIMAL_WARNING = 10;
    private static final String POMMES_SYMBOL_IDENTIFIER = "TEST";
    private static final List<ProductRequest> FIRST_FOOD_COURT_PRODUCT_REQUESTS = List.of(
        new ProductRequest(BURGER_MENU_MIT_COLA_ID, BURGER_MENU_MIT_COLA_PRICE, BURGER_MENU_MIT_COLA, BURGER_MENU_MIT_COLA_SYMBOL_IDENTIFIER, BURGER_MENU_MIT_COLA_MINIMAL_WARNING),
        new ProductRequest(BURGER_MENU_ID, BURGER_MENU_PRICE, BURGER_MENU, BURGER_MENU_SYMBOL_IDENTIFIER, BURGER_MENU_MINIMAL_WARNING),
        new ProductRequest(BURGER_ID, BURGER_PRICE, BURGER, BURGER_SYMBOL_IDENTIFIER, BURGER_MINIMAL_WARNING),
        new ProductRequest(COLA_ID, COLA_PRICE, COLA, COLA_SYMBOL_IDENTIFIER, COLA_MINIMAL_WARNING),
        new ProductRequest(POMMES_ID, POMMES_PRICE, POMMES, POMMES_SYMBOL_IDENTIFIER, POMMES_MINIMAL_WARNING)
    );

    private static final String PIZZA_MARGHERITA_MENU_MIT_COLA = "Pizza Margherita Menü mit Fanta";
    private static final UUID PIZZA_MARGHERITA_MENU_MIT_FANTA_ID = UUID.fromString("8d3cf1a0-7b8c-450f-8cdd-6714f672027c");
    private static final double PIZZA_MARGHERITA_MENU_MIT_COLA_PRICE = 12;
    private static final int PIZZA_MARGHERITA_MENU_MIT_COLA_MINIMAL_WARNING = 10;
    private static final String PIZZA_MARGHERITA_MENU_MIT_COLA_SYMBOL_IDENTIFIER = "TEST";
    private static final String PIZZA_MARGHERITA_MENU = "Pizza Margherita Menü";
    private static final UUID PIZZA_MARGHERITA_MENU_ID = UUID.fromString("a7dfc4a1-59f5-41d1-9cdd-c1e3ce8f0be6");
    private static final double PIZZA_MARGHERITA_MENU_PRICE = 11;
    private static final int PIZZA_MARGHERITA_MENU_MINIMAL_WARNING = 10;
    private static final String PIZZA_MARGHERITA_MENU_SYMBOL_IDENTIFIER = "TEST";
    private static final String PIZZA_MARGHERITA = "Pizza Margherita";
    private static final UUID PIZZA_MARGHERITA_ID = UUID.fromString("17c5680e-22d5-4647-b5bb-b4a586f6c6d2");
    private static final double PIZZA_MARGHERITA_RICE = 7.5;
    private static final int PIZZA_MARGHERITA_MINIMAL_WARNING = 10;
    private static final String PIZZA_MARGHERITA_SYMBOL_IDENTIFIER = "TEST";
    private static final String FANTA = "Fanta";
    private static final UUID FANTA_ID = UUID.fromString("8df2fa53-75b6-42c7-8798-c4157e756c41");
    private static final double FANTA_PRICE = 1.5;
    private static final int FANTA_MINIMAL_WARNING = 10;
    private static final String FANTA_SYMBOL_IDENTIFIER = "TEST";
    private static final String ANTIPASTI = "Antipasti";
    private static final UUID ANTIPASTI_ID = UUID.fromString("fe099c8c-06da-48b9-9c36-7df26322fe94");
    private static final double ANTIPASTI_PRICE = 4;
    private static final int ANTIPASTI_MINIMAL_WARNING = 10;
    private static final String ANTIPASTI_SYMBOL_IDENTIFIER = "TEST";
    private static final List<ProductRequest> SECOND_FOOD_COURT_PRODUCT_REQUESTS = List.of(
        new ProductRequest(PIZZA_MARGHERITA_MENU_MIT_FANTA_ID, PIZZA_MARGHERITA_MENU_MIT_COLA_PRICE, PIZZA_MARGHERITA_MENU_MIT_COLA, PIZZA_MARGHERITA_MENU_MIT_COLA_SYMBOL_IDENTIFIER, PIZZA_MARGHERITA_MENU_MIT_COLA_MINIMAL_WARNING),
        new ProductRequest(PIZZA_MARGHERITA_MENU_ID, PIZZA_MARGHERITA_MENU_PRICE, PIZZA_MARGHERITA_MENU, PIZZA_MARGHERITA_MENU_SYMBOL_IDENTIFIER, PIZZA_MARGHERITA_MENU_MINIMAL_WARNING),
        new ProductRequest(PIZZA_MARGHERITA_ID, PIZZA_MARGHERITA_RICE, PIZZA_MARGHERITA, PIZZA_MARGHERITA_SYMBOL_IDENTIFIER, PIZZA_MARGHERITA_MINIMAL_WARNING),
        new ProductRequest(FANTA_ID, FANTA_PRICE, FANTA, FANTA_SYMBOL_IDENTIFIER, FANTA_MINIMAL_WARNING),
        new ProductRequest(ANTIPASTI_ID, ANTIPASTI_PRICE, ANTIPASTI, ANTIPASTI_SYMBOL_IDENTIFIER, ANTIPASTI_MINIMAL_WARNING)
    );

    /*
        PRODUCT LINKS
    */
    private static final List<ProductLinkRequest> PRODUCT_LINK_REQUESTS = List.of(
        new ProductLinkRequest(BURGER_MENU_MIT_COLA_ID, BURGER_ID),
        new ProductLinkRequest(BURGER_MENU_MIT_COLA_ID, POMMES_ID),
        new ProductLinkRequest(BURGER_MENU_MIT_COLA_ID, COLA_ID),
        new ProductLinkRequest(BURGER_MENU_ID, BURGER_ID),
        new ProductLinkRequest(BURGER_MENU_ID, COLA_ID),
        new ProductLinkRequest(PIZZA_MARGHERITA_MENU_MIT_FANTA_ID, PIZZA_MARGHERITA_ID),
        new ProductLinkRequest(PIZZA_MARGHERITA_MENU_MIT_FANTA_ID, ANTIPASTI_ID),
        new ProductLinkRequest(PIZZA_MARGHERITA_MENU_MIT_FANTA_ID, FANTA_ID),
        new ProductLinkRequest(PIZZA_MARGHERITA_MENU_ID, PIZZA_MARGHERITA_ID),
        new ProductLinkRequest(PIZZA_MARGHERITA_MENU_ID, ANTIPASTI_ID)
    );

    /*
        CART ITEMS
     */
    private static final List<CartItemCreationRequest> FIRST_CART_ITEM_CREATION_REQUESTS = List.of(
            new CartItemCreationRequest(BURGER_MENU_ID, 2, ""),
            new CartItemCreationRequest(BURGER_MENU_ID, 1, "mit extra Käse"),
            new CartItemCreationRequest(BURGER_MENU_MIT_COLA_ID, 2, "")
    );

    private static final List<CartItemCreationRequest> SECOND_CART_ITEM_CREATION_REQUESTS = List.of(
            new CartItemCreationRequest(BURGER_MENU_ID, 2, ""),
            new CartItemCreationRequest(BURGER_MENU_ID, 1, "mit extra Käse"),
            new CartItemCreationRequest(BURGER_MENU_MIT_COLA_ID, 2, ""),
            new CartItemCreationRequest(PIZZA_MARGHERITA_ID, 2, ""),
            new CartItemCreationRequest(PIZZA_MARGHERITA_MENU_ID, 2, "mit extra Knoblauch"),
            new CartItemCreationRequest(ANTIPASTI_ID, 2, "")
    );


    private final AccountService accountService;
    private final CreditService creditService;
    private final ProductService productService;
    private final FoodCourtService foodCourtService;
    private final CartService cartService;
    private final FoodOrderService foodOrderService;
    private final FoodCourtSimulationService simulationService;

    public MasterEndpointImpl(AccountService accountService, CreditService creditService, ProductService productService, FoodCourtService foodCourtService, CartService cartService, FoodOrderService foodOrderService, FoodCourtSimulationService simulationService) {
        this.accountService = accountService;
        this.creditService = creditService;
        this.productService = productService;
        this.foodCourtService = foodCourtService;
        this.cartService = cartService;
        this.foodOrderService = foodOrderService;
        this.simulationService = simulationService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Operation(summary = "create initial data")
    @APIResponse(responseCode = "200", description = "Initial Data Created or already existing")
    public Response createInitialData() {
        accountService.createTickets(TICKET_LOGIN_NRS);
        accountService.createAccounts(ACCOUNT_REGISTER_REQUESTS);
        foodCourtService.createFoodCourts(FOOD_COURT_REQUESTS);
        productService.createProducts(FOOD_COURT_1_ID, FIRST_FOOD_COURT_PRODUCT_REQUESTS);
        productService.createProducts(FOOD_COURT_2_ID, SECOND_FOOD_COURT_PRODUCT_REQUESTS);
        productService.createLinks(PRODUCT_LINK_REQUESTS);
        try {
            creditService.changeAmount(V_000_000_001, new CreditAddRequest(10000));
            creditService.changeAmount(V_000_000_002, new CreditAddRequest(1000));
        } catch (ServiceException e) {
            LOG.error("could not increase amount; Exception: ", e);
        }
        FIRST_CART_ITEM_CREATION_REQUESTS.stream()//
                    .map(request -> {
                        try {
                            return cartService.addItemToCart(V_000_000_001, request);
                        } catch (ServiceException e) {
                            LOG.error("could not add item to cart; Exception: ", e);
                            return null;
                        }
                    })//
                .filter(Objects::nonNull)//
                .toList()//
        ;

        SECOND_CART_ITEM_CREATION_REQUESTS.stream()//
                .map(request -> {
                    try {
                        return cartService.addItemToCart(V_000_000_002, request);
                    } catch (ServiceException e) {
                        LOG.error("could not add item to cart; Exception: ", e);
                        return null;
                    }
                })//
                .filter(Objects::nonNull)//
                .toList()//
        ;

        try {
            foodOrderService.create(V_000_000_001);
        } catch (ServiceException e) {
            LOG.error("could not create order for {{}}; Exception: ", V_000_000_001, e);
        }

        try {
            foodOrderService.create(V_000_000_002);
        } catch (ServiceException e) {
            LOG.error("could not create order for {{}}; Exception: ", V_000_000_002, e);
        }

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Operation(summary = "get all data")
    public Response getALLData(@HeaderParam("Authorization") String auth) {
        System.out.println("Authorization header: " + auth);
        DatabaseResponse data = accountService.getDatabaseResponse();
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Path("simmulation/pause")
    public String pause() {
        simulationService.pauseSimulation();
        return "Simulation paused";
    }

    @POST
    @Path("simmulation/resume")
    public String resume() {
        simulationService.resumeSimulation();
        return "Simulation resumed";
    }
}

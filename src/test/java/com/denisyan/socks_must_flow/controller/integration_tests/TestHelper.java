package com.denisyan.socks_must_flow.controller.integration_tests;

public class TestHelper {

    public static final String SOCK_GREEN_50_JSON =
            "{" +
                    "\"color\": \"green\"," +
                    "\"cottonPart\":50," +
                    "\"quantity\": 40" +
                    "}";

    public static final String SOCK_RED_90_JSON_Q50 =
            "{" +
                    "\"color\": \"red\"," +
                    "\"cottonPart\":90," +
                    "\"quantity\": 50" +
                    "}";

    public static final String SOCK_RED_90_JSON_Q100 =
            "{" +
                    "\"color\": \"red\"," +
                    "\"cottonPart\":90," +
                    "\"quantity\": 100" +
                    "}";



    public static final String SOCK_GREEN_50_NEGATIVE_QUANTITY_JSON =
            "{" +
                    "\"color\": \"green\"," +
                    "\"cottonPart\":50," +
                    "\"quantity\": -100" +
                    "}";

    public static final String  SOCK_GREEN_OUT_OF_RANGE_COTTON_PART_JSON =
            "{" +
                    "\"color\": \"green\"," +
                    "\"cottonPart\":500," +
                    "\"quantity\": 40" +
                    "}";

    public static final String SOCK_COLOR_NOT_EXIST_50_JSON =
            "{" +
                    "\"color\": \"aaaaaaa\"," +
                    "\"cottonPart\":50," +
                    "\"quantity\": 40" +
                    "}";

    public static final String SOCK_EMPTY_COLOR_AND_ZERO_QUANTITY =
            "{" +
                    "\"color\": \"\"," +
                    "\"cottonPart\":0," +
                    "\"quantity\": 0" +
                    "}";

    public static final String SOCK_EMPTY_BODY =
            "{}";

    public static final String  SOCK_GREEN_50_ZERO_QUANTITY_JSON =
            "{" +
                    "\"color\": \"green\"," +
                    "\"cottonPart\":50," +
                    "\"quantity\": 0" +
                    "}";

    public static final String CORRECT_WAREHOUSEMAN =
            "{" +
                    "\"name\": \"Ivan\"," +
                    "\"surname\":\"Alekseev\"," +
                    "\"login\": \"MEGAVANYA\"," +
                    "\"password\": \"QWERTY12345\"" +
                    "}";

    public static final String CORRECT_WAREHOUSEMAN_1 =
            "{" +
                    "\"name\": \"Artem\"," +
                    "\"surname\":\"Alekseev\"," +
                    "\"login\": \"MEGAARTEM\"," +
                    "\"password\": \"QWERTY123456\"" +
                    "}";

    public static final String SUPERUSER =
            "{" +
                    "\"name\": \"admin\"," +
                    "\"surname\":\"admin\"," +
                    "\"login\": \"admin\"," +
                    "\"password\": \"admin\"" +
                    "}";
}

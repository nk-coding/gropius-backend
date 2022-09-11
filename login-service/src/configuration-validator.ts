import * as Joi from "joi";

export const validationSchema = Joi.object({
    GROPIUS_INTERNAL_BACKEND_ENDPOINT: Joi.string().uri().required(),
    GROPIUS_INTERNAL_BACKEND_TOKEN: Joi.string(),
    GROPIUS_INTERNAL_BACKEND_JWT_SECRET: Joi.string().required(),

    GROPIUS_LOGIN_DATABASE_HOST: Joi.string().default("localhost"),
    GROPIUS_LOGIN_DATABASE_PORT: Joi.number()
        .integer()
        .min(0)
        .max(65535)
        .default(5432),
    GROPIUS_LOGIN_DATABASE_USER: Joi.string().default("postgres"),
    GROPIUS_LOGIN_DATABASE_PASSWORD: Joi.string().default("postgres"),
    GROPIUS_LOGIN_DATABASE_DATABASE: Joi.string().default("gropius"),

    GROPIUS_REGISTRATION_EXPIRATION_TIME_SEC: Joi.number().min(0).default(600),

    GROPIUS_PASSPORT_STATE_JWT_SECRET: Joi.string().required(),
    GROPIUS_PASSPORT_STATE_JWT_ISSUER: Joi.string().default(
        "gropius-login-state",
    ),
});

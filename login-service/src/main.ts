import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import { SwaggerModule, DocumentBuilder } from "@nestjs/swagger";
import { TokenScope } from "./backend-services/token.service";
import { OpenApiTag } from "./openapi-tag";
import { ConfigModule } from "@nestjs/config";

async function bootstrap() {
    const app = await NestFactory.create(AppModule);

    const openApiConfig = new DocumentBuilder()
        .setTitle("Gropius Login Service")
        .setDescription("API for login, registration and linking Gropius accounts to accounts on IMSs")
        .addTag(OpenApiTag.LOGIN_API, "Endpoints to interact with the model, register and link authentications")
        .addTag(OpenApiTag.SYNC_API, "API to be used by sync services for exchanging IMSUser info")
        .addTag(OpenApiTag.CREDENTIALS, "Endpoints for actual authentication. Token retrieval, oauth flow, ...")
        .addOAuth2({
            type: "oauth2",
            description: "Access token provided by running the oauth flow (and if needed) registering/linking",
            bearerFormat: "JWT",
        })
        .addBearerAuth({
            type: "apiKey",
            description: "Access token provided after running the oauth flow (and if needed registering/linking)",
        })
        .addApiKey({
            name: OpenApiTag.SYNC_API,
            type: "apiKey",
            description: "Secret Text shared between sync services and login service",
        })
        .build();
    let runningIndex = 0;
    const openApiDocument = SwaggerModule.createDocument(app, openApiConfig, {
        operationIdFactory(controllerKey, methodKey) {
            runningIndex = (runningIndex + 1) % (Number.MAX_SAFE_INTEGER - 1);
            return controllerKey + "_" + methodKey + "_" + runningIndex;
        },
    });
    SwaggerModule.setup("login-api-doc", app, openApiDocument);

    await ConfigModule.envVariablesLoaded;
    const portNumber = parseInt(process.env.GROPIUS_LOGIN_LISTEN_PORT, 10) || 3000;

    app.enableCors();
    await app.listen(portNumber);
}
bootstrap();

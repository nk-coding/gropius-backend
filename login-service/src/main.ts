import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import { SwaggerModule, DocumentBuilder } from "@nestjs/swagger";
import { TokenScope } from "./backend-services/token.service";

async function bootstrap() {
    const app = await NestFactory.create(AppModule);

    const openApiConfig = new DocumentBuilder()
        .setTitle("Gropius Login Service")
        .setDescription("API for login, registration and linking gropius accounts to accounts on IMSs")
        .addTag("login")
        .addOAuth2({
            type: "oauth2",
            description: "Access token provided by running the oauth flow (and if needed) registering/linking",
            bearerFormat: "JWT",
        })
        .addBearerAuth({
            type: "apiKey",
            description:
                "Access or registration token provided after running the oauth flow (and if needed registering/linking)",
            bearerFormat: "JWT",
        })
        .build();
    const openApiDocument = SwaggerModule.createDocument(app, openApiConfig);
    SwaggerModule.setup("login-api-doc", app, openApiDocument);

    app.enableCors();
    await app.listen(3000);
}
bootstrap();

import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import { SwaggerModule, DocumentBuilder } from "@nestjs/swagger";

async function bootstrap() {
    const app = await NestFactory.create(AppModule);

    const openApiConfig = new DocumentBuilder()
        .setTitle("Gropius Login Service")
        .setDescription(
            "API for login, registration and linking gropius accounts to accounts on IMSs",
        )
        .addTag("login")
        .build();
    const openApiDocument = SwaggerModule.createDocument(app, openApiConfig);
    SwaggerModule.setup("login-api-doc", app, openApiDocument);

    app.enableCors();
    await app.listen(3000);
}
bootstrap();

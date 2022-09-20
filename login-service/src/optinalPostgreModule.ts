import { DynamicModule, Module } from "@nestjs/common";
import { ConfigModule } from "@nestjs/config";
import { TypeOrmModule } from "@nestjs/typeorm";
import { EntityClassOrSchema } from "@nestjs/typeorm/dist/interfaces/entity-class-or-schema.type";

async function waitAndGetStubModule(): Promise<DynamicModule | null> {
    await ConfigModule.envVariablesLoaded;
    const disable = process.env.GROPIUS_LOGIN_DATABASE_DISABLE_POSTGRES;
    console.log(disable);
    if (!disable || disable == "false" || disable == "no") {
        return null;
    } else {
        return {
            module: StubModule,
            global: false,
        };
    }
}

export async function optioalGlobalTypeOrm(): Promise<DynamicModule> {
    return (
        (await waitAndGetStubModule()) ??
        TypeOrmModule.forRootAsync({
            useFactory(...args) {
                return {
                    type: "postgres",
                    host: process.env.GROPIUS_LOGIN_DATABASE_HOST,
                    port: parseInt(process.env.GROPIUS_LOGIN_DATABASE_PORT, 10),
                    username: process.env.GROPIUS_LOGIN_DATABASE_USER,
                    password: process.env.GROPIUS_LOGIN_DATABASE_PASSWORD,
                    database: process.env.GROPIUS_LOGIN_DATABASE_DATABASE,
                    synchronize: process.env.NODE_ENV !== "production",
                    autoLoadEntities: true,
                };
            },
        })
    );
}

export async function optionalFeatureTypeOrm(entities: EntityClassOrSchema[]): Promise<DynamicModule> {
    return (await waitAndGetStubModule()) ?? TypeOrmModule.forFeature(entities);
}

@Module({})
class StubModule {}

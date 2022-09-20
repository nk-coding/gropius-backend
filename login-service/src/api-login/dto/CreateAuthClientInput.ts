import { HttpException, HttpStatus } from "@nestjs/common";

export interface CreateOrUpdateAuthClientInput {
    redirectUrls?: string[];
    isValid?: boolean;
    requiresSecret?: boolean;
}

export function createOrUpdateAuthClientInputCheck(
    input: CreateOrUpdateAuthClientInput,
): CreateOrUpdateAuthClientInput {
    if (
        input.redirectUrls != undefined &&
        (!(input.redirectUrls instanceof Array) ||
            input.redirectUrls.length <= 0)
    ) {
        throw new HttpException(
            "If redirect URLs are given, they must be an array of valid url string containing at least one entry",
            HttpStatus.BAD_REQUEST,
        );
    }
    for (const url of input.redirectUrls) {
        if (typeof url !== "string") {
            throw new HttpException(
                "All given redirect urls must be valid url strings",
                HttpStatus.BAD_REQUEST,
            );
        }
        try {
            new URL(url);
        } catch (err) {
            throw new HttpException(
                "Invalid redirect url: " + err.message ?? err,
                HttpStatus.BAD_REQUEST,
            );
        }
    }
    if (input.isValid != undefined && typeof input.isValid !== "boolean") {
        throw new HttpException(
            "If isValid is given, it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (
        input.requiresSecret != undefined &&
        typeof input.requiresSecret !== "boolean"
    ) {
        throw new HttpException(
            "If requiresSecret is given, it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    return input;
}

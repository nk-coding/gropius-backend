import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { ActiveLogin } from "./ActiveLogin";
import * as bcrypt from "bcrypt";
import * as crypto from "crypto";
import { promisify } from "util";

const randomBytesAsync = promisify(crypto.randomBytes);

@Entity()
export class AuthClient {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column("json")
    redirectUrls: string[];

    @Column("json")
    clientSecrets: string[];

    @Column()
    isValid: boolean;

    @Column()
    requiresSecret: boolean;

    @OneToMany(() => ActiveLogin, (login) => login.createdByClient)
    loginsOfThisClient: Promise<ActiveLogin[]>;

    private fingerprint(hash: string): string {
        return crypto.createHash("sha256").update(hash).digest("hex");
    }

    async addSecret(): Promise<{
        secretText: string;
        fingerprint: string;
        censored: string;
    }> {
        const length = Math.min(
            15,
            parseInt(process.env.GROPIUS_CLIENT_SECRET_LENGTH, 10),
        );
        const secretText = (await randomBytesAsync(length)).toString("hex");
        if (secretText.length < 15) {
            throw new Error("Secret must be at least 15 characters long");
        }
        if (secretText.match(/[^a-zA-Z0-9+/-_=]/)) {
            throw new Error("Secret can not match /[^a-zA-Z0-9+/-_=]/");
        }
        const hash = await bcrypt.hash(
            secretText,
            parseInt(process.env.GROPIUS_BCRYPT_HASH_ROUNDS, 10),
        );
        const censored = secretText.substring(0, 5);
        if (!this.clientSecrets?.length || !this.clientSecrets?.push) {
            this.clientSecrets = [];
        }
        this.clientSecrets.push(censored + ";" + hash);

        return {
            secretText,
            fingerprint: this.fingerprint(hash),
            censored: censored + "**********",
        };
    }

    getFullHashesPlusCensoredAndFingerprint(): {
        secret: string;
        censored: string;
        fingerprint: string;
    }[] {
        return this.clientSecrets.map((s) => {
            const semiIndex = s.indexOf(";");
            const censored = s.substring(0, semiIndex) + "**********";
            const hash = s.substring(semiIndex + 1);

            return {
                secret: s,
                censored,
                fingerprint: this.fingerprint(hash),
            };
        });
    }

    getSecretsShortedAndFingerprint(): {
        censored: string;
        fingerprint: string;
    }[] {
        return this.getFullHashesPlusCensoredAndFingerprint().map((entry) => ({
            censored: entry.censored,
            fingerprint: entry.fingerprint,
        }));
    }

    toJSON() {
        return {
            id: this.id,
            redirectUrls: this.redirectUrls,
            isValid: this.isValid,
            requiresSecret: this.requiresSecret,
        };
    }
}

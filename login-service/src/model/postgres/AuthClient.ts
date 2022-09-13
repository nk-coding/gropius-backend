import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { ActiveLogin } from "./ActiveLogin";

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
}

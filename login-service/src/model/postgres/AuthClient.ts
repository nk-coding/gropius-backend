import { Column, Entity, PrimaryGeneratedColumn } from "typeorm";

@Entity()
export class AuthClient {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column("json")
    redirectUrls: string[];

    @Column("json")
    clientSecrets: string[];
}

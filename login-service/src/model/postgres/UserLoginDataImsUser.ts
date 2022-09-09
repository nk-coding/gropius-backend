import { Column, Entity, ManyToOne, PrimaryColumn } from "typeorm";
import { UserLoginData } from "./UserLoginData";

@Entity()
export class UserLoginDataImsUser {
    @PrimaryColumn("uuid")
    neo4jId: string;

    @ManyToOne(() => UserLoginData, (loginData) => loginData.imsUsers)
    loginData: Promise<UserLoginData>;

    toJSON() {
        return {};
    }
}

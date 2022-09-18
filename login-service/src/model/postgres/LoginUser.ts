import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { UserLoginData } from "./UserLoginData";

@Entity()
export class LoginUser {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column({ unique: true })
    neo4jId: string;

    @Column({ unique: true })
    username: string;

    @OneToMany(() => UserLoginData, (loginData) => loginData.user)
    loginData: Promise<UserLoginData[]>;

    @Column()
    revokeTokensBefore: Date;

    toJSON() {
        return {
            id: this.id,
            username: this.username,
        };
    }
}

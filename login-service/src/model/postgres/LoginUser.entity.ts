import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { UserLoginData } from "./UserLoginData.entity";

@Entity()
export class LoginUser {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column({ unique: true, nullable: true })
    neo4jId: string | null;

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

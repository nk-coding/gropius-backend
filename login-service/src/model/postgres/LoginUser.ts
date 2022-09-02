import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { UserLoginData } from "./UserLoginData";

@Entity()
export class LoginUser {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column()
    neo4jId: string;

    @Column()
    username: string;

    @Column()
    displayName: string;

    @Column({ nullable: true })
    email: string | null;

    @OneToMany(() => UserLoginData, (loginData) => loginData.user)
    loginData: UserLoginData[];
}

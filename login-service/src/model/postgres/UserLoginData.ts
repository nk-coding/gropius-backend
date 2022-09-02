import {
    Column,
    Entity,
    ManyToOne,
    OneToMany,
    PrimaryGeneratedColumn,
} from "typeorm";
import { LoginUser } from "./LoginUser";
import { StrategyInstance } from "./StrategyInstance";
import { UserLoginDataImsUser } from "./UserLoginDataImsUser";

@Entity()
export class UserLoginData {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @ManyToOne(() => LoginUser, (user) => user.loginData)
    user: LoginUser;

    @ManyToOne(() => StrategyInstance)
    strategyInstance: StrategyInstance;

    @Column("json")
    data: any;

    @OneToMany(() => UserLoginDataImsUser, (imsUser) => imsUser.loginData)
    imsUsers: UserLoginDataImsUser[];

    toJSON() {
        return { id: this.id };
    }
}

import {
    Column,
    Entity,
    ManyToOne,
    OneToMany,
    PrimaryGeneratedColumn,
} from "typeorm";
import { ActiveLogin } from "./ActiveLogin";
import { LoginUser } from "./LoginUser";
import { StrategyInstance } from "./StrategyInstance";
import { UserLoginDataImsUser } from "./UserLoginDataImsUser";

/**
 * Data used for verifying a login by a LoginUser.
 *
 * For example password for username/password auth or the username for GitHub auth
 */
@Entity()
export class UserLoginData {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @ManyToOne(() => LoginUser, (user) => user.loginData)
    user: LoginUser;

    @ManyToOne(() => StrategyInstance)
    strategyInstance: StrategyInstance;

    /**
     * Data which needs to be stored for the user for every strategy he uses (e.g. username on the 3rd party platform)
     */
    @Column("json")
    data: any;

    @OneToMany(() => UserLoginDataImsUser, (imsUser) => imsUser.loginData)
    imsUsers: UserLoginDataImsUser[];

    @OneToMany(() => ActiveLogin, (login) => login.loginInstanceFor)
    loginsUsingThis: ActiveLogin[];

    toJSON() {
        return { id: this.id };
    }
}

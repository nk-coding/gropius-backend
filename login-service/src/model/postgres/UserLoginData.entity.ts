import {
    Column,
    Entity,
    JoinColumn,
    ManyToOne,
    OneToMany,
    PrimaryGeneratedColumn,
} from "typeorm";
import { ActiveLogin } from "./ActiveLogin.entity";
import { LoginUser } from "./LoginUser.entity";
import { StrategyInstance } from "./StrategyInstance.entity";
import { UserLoginDataImsUser } from "./UserLoginDataImsUser.entity";

export enum LoginState {
    /**
     * The user authenticated using a strategy but has no account yet.
     * The login data will be saved for some time to give the user time to register using the temporary access token
     */
    WAITING_FOR_REGISTER = "WAITING_FOR_REGISTER",
    VALID = "VALID",
    /**
     * The login of the user with this login data has been blocked by an administrator
     */
    BLOCKED = "BLOCKED",
}

/**
 * Data used for verifying a login by a LoginUser.
 *
 * For example password for username/password auth or the username for GitHub auth
 */
@Entity()
export class UserLoginData {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @ManyToOne(() => LoginUser, (user) => user.loginData, { nullable: true })
    user: Promise<LoginUser | null>;

    @ManyToOne(() => StrategyInstance)
    @JoinColumn({ name: "strategyInstanceId" })
    strategyInstance: Promise<StrategyInstance>;

    /**
     * Data which needs to be stored for the user for every strategy he uses (e.g. username on the 3rd party platform)
     */
    @Column("jsonb")
    data: any;

    @Column({
        type: "enum",
        enum: LoginState,
        default: LoginState.VALID,
    })
    state: LoginState;

    @Column({ nullable: true, default: null })
    expires: Date | null;

    @OneToMany(() => UserLoginDataImsUser, (imsUser) => imsUser.loginData)
    imsUsers: Promise<UserLoginDataImsUser[]>;

    @OneToMany(() => ActiveLogin, (login) => login.loginInstanceFor)
    loginsUsingThis: Promise<ActiveLogin[]>;

    toJSON() {
        return { id: this.id };
    }
}

import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { LoginUser } from "./LoginUser";
import { StrategyInstance } from "./StrategyInstance";
import { UserLoginData } from "./UserLoginData";

export enum LoginState {
    /**
     * The user authenticated using a strategy but has no account yet.
     * The login will be saved for some time to give the user time to register using the temporary access token
     */
    WAITING_FOR_REGISTER = "WAITING_FOR_REGISTER",
    VALID_LOGIN = "VALID_LOGIN",
    UNKNOWN = "UNKNOWN",
    /**
     * The login session has been set to invalid, e.g. because a refresh token was reused. No refresh token for this session may produce a valid access token any more
     */
    INVALID = "INVALID",
}

@Entity()
export class ActiveLogin {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    constructor(usedStrategyInstance: StrategyInstance) {
        this.usedStrategyInstnce = usedStrategyInstance;
    }

    @Column()
    expires: Date;

    @Column({
        type: "enum",
        enum: LoginState,
        default: LoginState.UNKNOWN,
    })
    state: LoginState;

    @Column({
        default: 0,
    })
    nextExpectedRefreshTokenNumber: number;

    /**
     * Data which needs to be stored on a per-login basis (e.g. issued tokens from auth provider)
     */
    @Column("json")
    data: object = {};

    /**
     * May not be null, must be set on creation.
     */
    @ManyToOne(() => StrategyInstance)
    usedStrategyInstnce: StrategyInstance;

    @ManyToOne(() => UserLoginData, (loginData) => loginData.loginsUsingThis, {
        nullable: true,
    })
    loginInstanceFor: UserLoginData | null;
}

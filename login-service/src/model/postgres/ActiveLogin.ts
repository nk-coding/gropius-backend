import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { AuthClient } from "./AuthClient";
import { LoginUser } from "./LoginUser";
import { StrategyInstance } from "./StrategyInstance";
import { UserLoginData } from "./UserLoginData";

@Entity()
export class ActiveLogin {
    static LOGGED_IN_BUT_TOKEN_NOT_YET_RETRIVED = -1;

    @PrimaryGeneratedColumn("uuid")
    id: string;

    constructor(usedStrategyInstance: StrategyInstance, expires?: Date) {
        this.usedStrategyInstnce = Promise.resolve(usedStrategyInstance);
        this.created = new Date();
        this.expires = expires || null;
        this.isValid = false;
        this.nextExpectedRefreshTokenNumber =
            ActiveLogin.LOGGED_IN_BUT_TOKEN_NOT_YET_RETRIVED;
    }

    @Column()
    created: Date;

    @Column({ nullable: true })
    expires: Date | null;

    @Column()
    isValid: boolean;

    @Column()
    supportsSync: boolean;

    @Column({
        default: 0,
    })
    nextExpectedRefreshTokenNumber: number;

    /**
     * Data which needs to be stored on a per-login basis (e.g. issued tokens from auth provider)
     */
    @Column("jsonb")
    data: object = {};

    /**
     * May not be null, must be set on creation.
     */
    @ManyToOne(() => StrategyInstance)
    usedStrategyInstnce: Promise<StrategyInstance>;

    @ManyToOne(() => UserLoginData, (loginData) => loginData.loginsUsingThis, {
        nullable: true,
    })
    loginInstanceFor: Promise<UserLoginData | null>;

    @ManyToOne(() => AuthClient, (client) => client.loginsOfThisClient, {
        nullable: true,
    })
    createdByClient: Promise<AuthClient | null>;
}

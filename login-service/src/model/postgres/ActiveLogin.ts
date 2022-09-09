import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { LoginUser } from "./LoginUser";
import { StrategyInstance } from "./StrategyInstance";
import { UserLoginData } from "./UserLoginData";

@Entity()
export class ActiveLogin {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    constructor(usedStrategyInstance: StrategyInstance, expires?: Date) {
        this.usedStrategyInstnce = Promise.resolve(usedStrategyInstance);
        this.created = new Date();
        this.expires = expires || null;
        this.isValid =
            this.expires == null ? true : this.created < this.expires;
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
}

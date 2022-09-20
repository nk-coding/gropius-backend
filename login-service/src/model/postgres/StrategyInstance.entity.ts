import { Column, Entity, PrimaryGeneratedColumn } from "typeorm";

@Entity()
export class StrategyInstance {
    constructor(type: string) {
        this.type = type;
    }

    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column({ nullable: true })
    name: string | null;

    @Column("jsonb")
    instanceConfig: object;

    @Column()
    readonly type: string;

    @Column()
    isLoginActive: boolean;

    @Column()
    isSelfRegisterActive: boolean;

    @Column()
    isSyncActive: boolean;

    @Column()
    doesImplicitRegister: boolean;

    toJSON() {
        return {
            id: this.id,
            name: this.name,
            type: this.type,
            isLoginActive: this.isLoginActive,
            isSelfRegisterActive: this.isSelfRegisterActive,
            isSyncActive: this.isSyncActive,
            doesImplicitRegister: this.doesImplicitRegister,
        };
    }
}

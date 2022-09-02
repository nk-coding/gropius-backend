import { Column, Entity, PrimaryGeneratedColumn } from "typeorm";

@Entity()
export class StrategyInstance {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column({ unique: true, nullable: true })
    name: string | null;

    @Column()
    type: string;

    @Column("json")
    instanceConfig: object;

    @Column()
    isLoginActive: boolean;

    @Column()
    isRegisterActive: boolean;

    @Column()
    isSyncActive: boolean;

    toJSON() {
        return {
            name: this.name,
            type: this.type,
            isLoginActive: this.isLoginActive,
            isRegisterActive: this.isRegisterActive,
            isSyncActive: this.isSyncActive,
        };
    }
}

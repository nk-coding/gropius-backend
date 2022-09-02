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
    instanceConfig: any;

    @Column()
    isLoginActive: boolean;

    @Column()
    isRegisterActive: boolean;

    @Column()
    isSyncActive: boolean;
}

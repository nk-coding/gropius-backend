import { Injectable } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { StrategyInstance } from "../postgres/StrategyInstance";
import { LoginState, UserLoginData } from "../postgres/UserLoginData";

@Injectable()
export class UserLoginDataService extends Repository<UserLoginData> {
    constructor(private dataSource: DataSource) {
        super(UserLoginData, dataSource.createEntityManager());
    }

    public findForStrategyWithDataContaining(
        strategyInstance: StrategyInstance,
        data: object,
    ): Promise<UserLoginData[]> {
        return this.createQueryBuilder("loginData")
            .where(`"strategyInstanceId" = :instanceId`, {
                instanceId: strategyInstance.id,
            })
            .andWhere(`("expires" is null or "expires" >= :dateNow)`, {
                dateNow: new Date(),
            })
            .andWhere(`"data" @> :data`, { data })
            .getMany();
    }

    /**
     * Variant for when database type of fields is json instead of jsonb
     */
    /*public findForStrategyWithDataContaining(
        strategyInstance: StrategyInstance,
        data: object,
    ): Promise<UserLoginData[]> {
        let builder = this.createQueryBuilder("loginData").where(
            `"strategyInstanceId" = :instanceId`,
            {
                instanceId: strategyInstance.id,
            },
        );
        let i = 0;
        for (const key in data) {
            if (Object.prototype.hasOwnProperty.call(data, key)) {
                const element = data[key];
                const variables = {};
                variables["key" + i] = key;
                variables["value" + i] = element;
                builder = builder.andWhere(
                    `("data"->:key${i}) = :value${i}`,
                    variables,
                );
                i++;
            }
        }
        return builder.getMany();
    }*/
}

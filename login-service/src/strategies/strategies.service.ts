import { Injectable } from "@nestjs/common";
import * as passport from "passport";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { Strategy } from "./Strategy";

@Injectable()
export class StrategiesService {
    private readonly allStrategies: Map<string, Strategy> = new Map();
    private readonly allPassportInstances: Map<string, passport.Strategy> =
        new Map();

    public getStrategyByName(name: string): Strategy | null {
        return this.allStrategies.get(name) ?? null;
    }

    public hasStrategy(name: string): boolean {
        return this.allStrategies.has(name);
    }

    public addStrategy(name: string, strategy: Strategy) {
        if (this.allStrategies.has(name)) {
            throw new Error(`Strategy with name ${name} already exists`);
        }
        this.allStrategies.set(name, strategy);
    }

    public getAllStrategies(): Strategy[] {
        return [...this.allStrategies.values()];
    }

    public getPassportStrategyInstanceFor(
        strategyInstance: StrategyInstance,
    ): passport.Strategy {
        if (this.allPassportInstances.has(strategyInstance.id)) {
            return this.allPassportInstances.get(strategyInstance.id);
        } else {
            const strategy = this.getStrategyByName(strategyInstance.type);
            const newInstance =
                strategy.getPassportStrategyInstance(strategyInstance);
            console.log(
                `Created new passport strategy for strategy ${strategy.typeName}, instance: ${strategyInstance.id}`,
            );
            this.allPassportInstances.set(strategyInstance.id, newInstance);
            return newInstance;
        }
    }
}

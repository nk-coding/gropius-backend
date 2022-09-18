export class CreateStrategyInstanceInput {
    name: string | null;
    instanceConfig: object;
    isLoginActive: boolean;
    isSelfRegisterActive: boolean;
    isSyncActive: boolean;
}

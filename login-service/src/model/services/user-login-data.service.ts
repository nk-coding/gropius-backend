import { Injectable } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { UserLoginData } from "../postgres/UserLoginData";

@Injectable()
export class UserLoginDataService extends Repository<UserLoginData> {
    constructor(private dataSource: DataSource) {
        super(UserLoginData, dataSource.createEntityManager());
    }
}

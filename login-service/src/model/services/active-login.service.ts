import { Injectable } from "@nestjs/common";
import { DataSource, Repository } from "typeorm";
import { ActiveLogin } from "../postgres/ActiveLogin";

@Injectable()
export class ActiveLoginService extends Repository<ActiveLogin> {
    constructor(private dataSource: DataSource) {
        super(ActiveLogin, dataSource.createEntityManager());
    }
}

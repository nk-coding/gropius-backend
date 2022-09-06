import { Body, Controller, Get } from "@nestjs/common";

@Controller("test")
export class TestController {
    @Get()
    getTest(@Body() body) {
        console.log(body);
        return "test";
    }
}

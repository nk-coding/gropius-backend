export function defaultReturn(operation: string) {
    return {
        result: "success",
        operation,
        time: Date.now(),
    };
}

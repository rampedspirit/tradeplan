type ConditionType = "AND" | "OR";

export class ConditionQuery {
    type: ConditionType;
    parentQuery: ConditionQuery;

    filterId: string;
    subQueries: ConditionQuery[];

    constructor(canHaveSubQueries: boolean) {
        this.type = "AND";
        if (canHaveSubQueries) {
            this.subQueries = [];
        }
    }

    isRoot(): boolean {
        return this.parentQuery == null;
    }

    add(subQuery: ConditionQuery) {
        if (this.subQueries) {
            subQuery.parentQuery = this;
            this.subQueries.push(subQuery);
        }
    }

    delete() {
        if (this.parentQuery) {
            this.parentQuery.subQueries = this.parentQuery.subQueries.filter(q => q != this);
        }
    }
}
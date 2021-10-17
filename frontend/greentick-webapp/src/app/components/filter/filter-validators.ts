import { AbstractControl, AsyncValidatorFn, ValidationErrors } from "@angular/forms";
import { Observable, of } from "rxjs";
import { map } from "rxjs/operators";
import { Filter, FilterService } from "src/gen/filter";

export class FilterValidators {

    static nospace(control: AbstractControl): ValidationErrors {
        if (/\s/g.test(control.value)) {
            return { "hasSpace": true };
        }
        return null;
    }

    static notunique(filterService: FilterService, exclude?: string[]): AsyncValidatorFn {
        return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
            if (exclude && exclude.find(s => s == control.value)) {
                return of(null);
            } else {
                return filterService.getAllFilters().pipe(map(
                    (filters: Filter[]) => {
                        return (filters && filters.find(f => f.name == control.value)) ? { "notUnique": true } : null;
                    }
                ));
            }
        };
    }
}
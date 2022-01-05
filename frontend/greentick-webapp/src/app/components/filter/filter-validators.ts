import { AbstractControl, AsyncValidatorFn, ValidationErrors } from "@angular/forms";
import { Observable, of } from "rxjs";
import { catchError, map } from "rxjs/operators";
import { Filter, FilterService } from "src/gen/filter";

export class FilterValidators {

    static noSpace(control: AbstractControl): ValidationErrors {
        if (/\s/g.test(control.value)) {
            return { "hasSpace": true };
        }
        return null;
    }

    static notUnique(filterService: FilterService, exclude: string[]): AsyncValidatorFn {
        return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
            if (exclude && exclude.find(s => s == control.value)) {
                return of(null);
            } else {
                return filterService.getAllFilters().pipe(map(
                    (filters: Filter[]) => {
                        return (filters && filters.find(f => f.name == control.value)) ? { "notUnique": true } : null;
                    }
                ), catchError(error => of({ "serviceNotReachable": true })));
            }
        };
    }

    static notUniqueWithCallback(filterService: FilterService, onStart: () => void, onEnd: () => void): AsyncValidatorFn {
        return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
            onStart();
            return filterService.getAllFilters().pipe(map(
                (filters: Filter[]) => {
                    onEnd();
                    return (filters && filters.find(f => f.name == control.value)) ? { "notUnique": true } : null;
                }
            ), catchError(error => {
                onEnd();
                return of({ "serviceNotReachable": true });
            }));
        };
    }
}
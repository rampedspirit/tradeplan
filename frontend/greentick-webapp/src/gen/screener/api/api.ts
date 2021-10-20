export * from './execution.service';
import { ExecutionService } from './execution.service';
export * from './result.service';
import { ResultService } from './result.service';
export * from './screener.service';
import { ScreenerService } from './screener.service';
export const APIS = [ExecutionService, ResultService, ScreenerService];

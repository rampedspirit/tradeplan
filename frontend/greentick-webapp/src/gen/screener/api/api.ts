export * from './executable.service';
import { ExecutableService } from './executable.service';
export * from './screener.service';
import { ScreenerService } from './screener.service';
export const APIS = [ExecutableService, ScreenerService];

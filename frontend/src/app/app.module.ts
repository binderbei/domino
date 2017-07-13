import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ModuleComponent } from './module/module.component';
import { RestService } from "./services/rest.service";
import { HttpModule } from "@angular/http";
import {GameService} from "./services/game.service";

@NgModule({
  declarations: [
    AppComponent,
    ModuleComponent
  ],
  imports: [
    BrowserModule,
    HttpModule
  ],
  providers: [RestService, GameService],
  bootstrap: [AppComponent]
})
export class AppModule { }

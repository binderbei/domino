import {EventEmitter, Injectable, Output} from '@angular/core';
import {Domino} from "../model/domino";

@Injectable()
export class GameService {
  private selected: Domino[] = []
  public openModule: number = -1;
  @Output() open: EventEmitter<any> = new EventEmitter();

  constructor() { }

  setSelected(moduleId: number, domino: Domino) {
    this.selected[moduleId] = domino;
    this.selected.forEach(t=>console.log(t))
  }
}

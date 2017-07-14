import {EventEmitter, Injectable, Output} from '@angular/core';
import {Domino} from "../model/domino";

@Injectable()
export class GameService {
  private selected: Map<number, Domino> = new Map<number, Domino>();
  public openModule: number = -1;
  @Output() open: EventEmitter<any> = new EventEmitter();

  constructor() { }

  setSelected(moduleNumber: number, domino: Domino) {
    this.selected.set(moduleNumber, domino);
  }

  getSelectedDomino(moduleNumber: number) {
    return this.selected.get(moduleNumber);
  }

  isSelected(moduleNumber: number) {
    return this.selected.has(moduleNumber);
  }

  removeSelect(moduleNumber: number) {
    return this.selected.delete(moduleNumber);
  }

  getAllSelectedDominos() {
    let selectedDominos: Domino[] = [];
    for (let domino of this.selected.values()) {
      selectedDominos.push(domino);
    }
    return selectedDominos;
  }
}

import {EventEmitter, Injectable, Output} from '@angular/core';
import {Domino} from '../model/domino';
import {isUndefined} from 'util';

@Injectable()
export class GameService {
  private selected: Map<number, Domino> = new Map<number, Domino>();
  private activeModules: string[];
  public openModule: number = -1;
  @Output() open: EventEmitter<any> = new EventEmitter();

  constructor() { }

  setSelected(moduleNumber: number, domino: Domino) {
    this.selected.set(moduleNumber, domino);
  }

  setActiveModules(activeModules: string[]) {
    this.activeModules = activeModules;
  }

  isModuleActive(moduleId: number) {
    if (isUndefined(this.activeModules)) {
      return true;
    } else {
      return this.activeModules.indexOf(moduleId.toString()) > -1;
    }
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
    const selectedDominos: Domino[] = [];
    for (const domino of this.selected.values()) {
      selectedDominos.push(domino);
    }
    return selectedDominos;
  }
}

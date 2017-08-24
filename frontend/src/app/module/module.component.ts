import {Component, OnInit, Input} from '@angular/core';
import {RestService} from "../services/rest.service";
import {Domino} from "../model/domino";
import {GameService} from "../services/game.service";

@Component({
  selector: 'app-module',
  templateUrl: './module.component.html',
  styleUrls: ['./module.component.css']
})
export class ModuleComponent implements OnInit {
  @Input() moduleNumber: number
  isOpen:boolean = this.gameService.openModule == this.moduleNumber;
  dominos: Domino[];

  constructor(private restService: RestService, private gameService: GameService) { }

  ngOnInit() {
    this.gameService.open
      .subscribe(item => {this.isOpen = this.gameService.openModule == this.moduleNumber});
  }

  clicked() {
    this.gameService.openModule = this.moduleNumber;
    this.isOpen = !this.isOpen && (this.gameService.openModule == this.moduleNumber);
    if (this.isOpen) {
      this.gameService.open.emit(null);
      this.restService.getDominosForModuleId(this.moduleNumber, this.gameService.getAllSelectedDominos()).subscribe(dominos => this.dominos = dominos);
    } else {
      this.dominos = [];
    }
  }

  isSelected() {
    return this.gameService.isSelected(this.moduleNumber);
  }

  getSelected() {
    return this.gameService.getSelectedDomino(this.moduleNumber);
  }

  getBackgroundIn(domino) {
    var colorLeft = domino.colorsIn[0];
    var colorRight = domino.colorsIn[1];
    return 'linear-gradient(to left, ' + colorLeft + ' 0% , ' + colorLeft + ' 50%, ' + colorRight + ' 50%,' + colorRight + ' 100%)';
  }

  getBackgroundOut(domino) {
    var colorLeft = domino.colorsOut[0];
    var colorRight = domino.colorsOut[1];
    return 'linear-gradient(to left, ' + colorLeft + ' 0% , ' + colorLeft + ' 50%, ' + colorRight + ' 50%,' + colorRight + ' 100%)';
  }

  dominoDeselect() {
    this.gameService.removeSelect(this.moduleNumber);
    this.gameService.openModule = this.moduleNumber;
    this.isOpen = true;
    if (this.isOpen) {
      this.gameService.open.emit(null);
      this.restService.getDominosForModuleId(this.moduleNumber, this.gameService.getAllSelectedDominos()).subscribe(dominos => this.dominos = dominos);
    } else {
      this.dominos = [];
    }
  }

  dominoSelected(domino: Domino) {
    this.gameService.setSelected(this.moduleNumber, domino);
  }

}

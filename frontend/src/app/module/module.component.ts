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
      .subscribe(item => {console.log("what"); this.isOpen = this.gameService.openModule == this.moduleNumber});
  }

  clicked() {
    this.gameService.openModule = this.moduleNumber;
    this.isOpen = !this.isOpen && (this.gameService.openModule == this.moduleNumber);
    console.log(this.isOpen);
    if (this.isOpen) {
      this.gameService.open.emit(null);
      this.restService.getDominosForModuleId(this.moduleNumber).subscribe(dominos => this.dominos = dominos);
    } else {
      this.dominos = [];
    }
  }
  onOpen() {

  }

  dominoSelected(domino: Domino) {
    this.gameService.setSelected(this.moduleNumber, domino);
  }

}

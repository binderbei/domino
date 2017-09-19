import { Injectable } from '@angular/core';
import {Http, Response, URLSearchParams} from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {Domino} from '../model/domino';
import {DominoData} from '../model/dominodata';

@Injectable()
export class RestService {

  constructor(private http: Http) { }

  getDominosForModuleId(moduleId: number, selected: Domino[]): Observable<DominoData> {
    const myParams: URLSearchParams = new URLSearchParams();
    for (const domino of selected) {
      myParams.append('selected', domino.dominoId);
    }
    return this.http.get('http://localhost:8088/rest-api/dominos/' + moduleId, {search: myParams})
      .map(this.extractData);
  }

  private extractData(res: Response) {
    const body = res.json();
    return body || { };
  }
}

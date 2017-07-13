import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {Domino} from "../model/domino";

@Injectable()
export class RestService {

  constructor(private http:Http) { }

  getDominosForModuleId(moduleId:number): Observable<Domino[]> {
    return this.http.get("http://localhost:8088/rest-api/dominos/" + moduleId)
      .map(this.extractData);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body || { };
  }
}

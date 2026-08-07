import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import {
  Attachment,
  AttachmentAnalysis
} from '../../Model/attachment.model';

@Component({
  selector: 'app-analysis',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './analysis.html',
  styleUrl: './analysis.css'
})
export class Analysis {

  analysis!: AttachmentAnalysis;

  attachment!: Attachment;


  constructor(private router: Router) {

    const state = history.state;

    this.analysis = state.analysis;

    this.attachment = state.attachment;


  }

  back() {
    this.router.navigate(['../']);
  }
}

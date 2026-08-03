import { TestBed } from '@angular/core/testing';

import { Assessment } from './assessment';

describe('Assessment', () => {
  let service: Assessment;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Assessment);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

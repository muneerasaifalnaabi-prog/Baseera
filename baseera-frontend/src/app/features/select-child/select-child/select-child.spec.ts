import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectChild } from './select-child';

describe('SelectChild', () => {
  let component: SelectChild;
  let fixture: ComponentFixture<SelectChild>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectChild],
    }).compileComponents();

    fixture = TestBed.createComponent(SelectChild);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

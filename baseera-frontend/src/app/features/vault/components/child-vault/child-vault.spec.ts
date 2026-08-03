import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildVault } from './child-vault';

describe('ChildVault', () => {
  let component: ChildVault;
  let fixture: ComponentFixture<ChildVault>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChildVault]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChildVault);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutWithSidebar } from './layout-with-sidebar';

describe('LayoutWithSidebar', () => {
  let component: LayoutWithSidebar;
  let fixture: ComponentFixture<LayoutWithSidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LayoutWithSidebar],
    }).compileComponents();

    fixture = TestBed.createComponent(LayoutWithSidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

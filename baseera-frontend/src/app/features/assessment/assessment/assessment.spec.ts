import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AssessmentPage } from './assessment';   

describe('AssessmentPage', () => {   
  let component: AssessmentPage;
  let fixture: ComponentFixture<AssessmentPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssessmentPage],   // ← standalone component
    }).compileComponents();

    fixture = TestBed.createComponent(AssessmentPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
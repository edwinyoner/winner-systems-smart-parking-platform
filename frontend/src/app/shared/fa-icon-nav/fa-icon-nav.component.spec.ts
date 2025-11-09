import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FaIconNavComponent } from './fa-icon-nav.component';

describe('FaIconNavComponent', () => {
  let component: FaIconNavComponent;
  let fixture: ComponentFixture<FaIconNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FaIconNavComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FaIconNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
